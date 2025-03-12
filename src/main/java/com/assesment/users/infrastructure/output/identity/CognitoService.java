package com.assesment.users.infrastructure.output.identity;

import com.assesment.users.domain.model.AuthenticatedUser;
import com.assesment.users.domain.model.User;
import com.assesment.users.infrastructure.output.identity.IdentityService;
import com.assesment.users.infrastructure.utils.GlobalConstants;
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
public class CognitoService implements IdentityService {

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

    public String registerUser(User user) {

            String secretHash = calculateSecretHash(clientId, clientSecret, user.getEmail());

            AdminCreateUserRequest signUpRequest = AdminCreateUserRequest .builder()
                    .userPoolId(userPoolId)
                    .username(user.getEmail())
                    .temporaryPassword(user.getPassword())
                    .userAttributes(
                            AttributeType.builder().name(GlobalConstants.EMAIL).value(user.getEmail()).build()
                    )
                    .messageAction(GlobalConstants.SUPRESS_ACTION)
                    .clientMetadata(Map.of(GlobalConstants.SECRET_HASH, secretHash))

                    .build();

            AdminCreateUserResponse registrationResult = cognitoClient.adminCreateUser(signUpRequest);

            AdminSetUserPasswordRequest setPasswordRequest = AdminSetUserPasswordRequest.builder()
                    .userPoolId(userPoolId)
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .permanent(true)
                    .build();

            cognitoClient.adminSetUserPassword(setPasswordRequest);

            AdminAddUserToGroupRequest addUserToGroupRequest = AdminAddUserToGroupRequest.builder()
                    .userPoolId(userPoolId)
                    .username(user.getEmail())
                    .groupName(user.getRole())
                    .build();

            cognitoClient.adminAddUserToGroup(addUserToGroupRequest);
            return registrationResult.user().username();

    }

    public static String calculateSecretHash(String clientId, String clientSecret, String username) {
        try {
            String message = username + clientId;
            Mac mac = Mac.getInstance(GlobalConstants.HASH);
            mac.init(new SecretKeySpec(clientSecret.getBytes(), GlobalConstants.HASH));
            byte[] hash = mac.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error calculating secret hash", e);
        }
    }

    public AuthenticatedUser loginUser(User user) {
        try {
            String secretHash = calculateSecretHash(clientId, clientSecret, user.getEmail());

            InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                    .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .clientId(clientId)
                    .authParameters(Map.of(
                            GlobalConstants.USERNAME, user.getEmail(),
                            GlobalConstants.PASSWORD, user.getPassword(),
                            GlobalConstants.SECRET_HASH, secretHash
                    ))
                    .build();

            InitiateAuthResponse authResponse = cognitoClient.initiateAuth(authRequest);
            AuthenticationResultType result = authResponse.authenticationResult();

            return new AuthenticatedUser(result.accessToken(),
                    result.refreshToken(),
                    result.expiresIn(),
                    result.tokenType());
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
