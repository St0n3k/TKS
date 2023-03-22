import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


@Testcontainers
public abstract class SoapTestcontainersSetup {


    protected static int payaraPort;
    protected static GenericContainer postgresContainer;
    protected static GenericContainer payaraContainer;

    @BeforeAll
    public static void setup() throws IOException {
        Network network = Network.SHARED;
        MountableFile warFile;
        String warFilePath = "../SoapAdapter/target/SoapAdapter-1.0.war";

        warFile = MountableFile.forHostPath(
                Paths.get(new File(warFilePath).getCanonicalPath()).toAbsolutePath(), 0777);

        postgresContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("pas")
                .withUsername("pas")
                .withPassword("pas")
                .withNetwork(network)
                .withNetworkAliases("db")
                .withReuse(true);

        payaraContainer = new GenericContainer("payara/server-full:6.2023.2-jdk17")
                .withExposedPorts(8181)
                .dependsOn(postgresContainer)
                .withNetwork(network)
                .withNetworkAliases("payara")
                .withCopyFileToContainer(warFile, "/opt/payara/deployments/SoapAdapter-1.0.war")
                .waitingFor(Wait.forHttps("/soap/roomAPI?wsdl").allowInsecure())
                .withReuse(true);

        postgresContainer.start();
        payaraContainer.start();

        payaraPort = payaraContainer.getMappedPort(8181);
    }

    @AfterAll
    static void clearContainers() {
        postgresContainer.stop();
        payaraContainer.stop();
    }
}
