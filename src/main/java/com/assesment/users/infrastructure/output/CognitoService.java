package com.assesment.users.infrastructure.output;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;

@Service
public class CognitoService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final String userPoolId;
    private final String clientId;
    private final String clientSecret;

    public CognitoService(
            @Value("${aws.cognito.userPoolId}") String userPoolId,
            @Value("${aws.cognito.clientId}") String clientId,
            @Value("${aws.cognito.region}") String region,
            @Value("${aws.accessKey}") String accessKey,
            @Value("${aws.secretKey}") String secretKey,
            @Value("${aws.cognito.clientSecret}") String clientSecret) {

        this.userPoolId = userPoolId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        this.cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public String registerUser(String email, String password) {
        try {
            String secretHash = calculateSecretHash(clientId, clientSecret, email);
            System.out.println("COGNITO");
            AdminCreateUserRequest signUpRequest = AdminCreateUserRequest .builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .temporaryPassword(password)
                    .userAttributes(
                            AttributeType.builder().name("email").value(email).build()
                    )
                    .messageAction("SUPPRESS")
                    .clientMetadata(Map.of("SECRET_HASH", secretHash))

                    .build();

            AdminCreateUserResponse a = cognitoClient.adminCreateUser(signUpRequest);
            System.out.println(a.user().username());
            AdminSetUserPasswordRequest setPasswordRequest = AdminSetUserPasswordRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .password(password)
                    .permanent(true)
                    .build();

            cognitoClient.adminSetUserPassword(setPasswordRequest);

            AdminAddUserToGroupRequest addUserToGroupRequest = AdminAddUserToGroupRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .groupName("ROLE_ADMIN")
                    .build();

            cognitoClient.adminAddUserToGroup(addUserToGroupRequest);
            return a.user().username();
        } catch (CognitoIdentityProviderException e) {
            return "Error registering user in Cognito: " + e.awsErrorDetails().errorMessage();
        }
    }

    public static String calculateSecretHash(String clientId, String clientSecret, String username) {
        try {
            String message = username + clientId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(clientSecret.getBytes(), "HmacSHA256"));
            byte[] hash = mac.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error calculating secret hash", e);
        }
    }

    public String signInAndGenerateJwt(String email, String password) {
        try {
            // Step 1: Authenticate and get Cognito tokens
            String secretHash = calculateSecretHash(clientId, clientSecret, email);

            InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                    .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .clientId(clientId)
                    .authParameters(Map.of(
                            "USERNAME", email,
                            "PASSWORD", password,
                            "SECRET_HASH", secretHash
                    ))
                    .build();

            InitiateAuthResponse authResponse = cognitoClient.initiateAuth(authRequest);
            AuthenticationResultType result = authResponse.authenticationResult();

            return result.accessToken();
        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException("Authentication failed: " + e.awsErrorDetails().errorMessage());
        }
    }

    private String getUserGroup(String email) {
        AdminListGroupsForUserRequest groupsRequest = AdminListGroupsForUserRequest.builder()
                .username(email)
                .userPoolId(userPoolId)
                .build();

        AdminListGroupsForUserResponse response = cognitoClient.adminListGroupsForUser(groupsRequest);
        return response.groups().isEmpty() ? "NoGroupAssigned" : response.groups().get(0).groupName();
    }
}
