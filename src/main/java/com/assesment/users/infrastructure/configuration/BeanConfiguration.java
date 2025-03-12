package com.assesment.users.infrastructure.configuration;

import com.assesment.users.domain.api.UserServicePort;
import com.assesment.users.domain.spi.UserPersistencePort;
import com.assesment.users.domain.usecase.UserUseCase;
import com.assesment.users.infrastructure.output.identity.CognitoService;
import com.assesment.users.infrastructure.output.dynamodb.UserEntity;
import com.assesment.users.infrastructure.output.dynamodb.UserEntityMapper;
import com.assesment.users.infrastructure.output.dynamodb.UserPersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.dynamodb.region}")
    private String region;
    private final UserEntityMapper userEntityMapper;
    private final CognitoService cognitoService;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTable<UserEntity> userEntityTable(DynamoDbEnhancedClient enhancedClient) {
        return enhancedClient.table("Users", TableSchema.fromBean(UserEntity.class));
    }

    @Bean
    public UserPersistencePort userPersistencePort(){
        return new UserPersistenceAdapter( userEntityTable(dynamoDbEnhancedClient(dynamoDbClient())), userEntityMapper,cognitoService);
    }

    @Bean
    public UserServicePort userServicePort(){
        return new UserUseCase(userPersistencePort());
    }
}
