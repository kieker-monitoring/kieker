//package kieker.integration;
//
//import com.spotify.docker.client.DefaultDockerClient;
//import com.spotify.docker.client.DockerCertificateException;
//import com.spotify.docker.client.DockerClient;
//import com.spotify.docker.client.DockerException;
//import com.spotify.docker.client.messages.ContainerConfig;
//import com.spotify.docker.client.messages.ContainerCreation;
//import org.junit.rules.ExternalResource;
//
//public class DockerContainerRule extends ExternalResource {
//
//  final DockerClient dockerClient;
//  ContainerCreation container;
//
//  public DockerContainerRule(String imageName, String[] ports, String cmd) {
//    dockerClient = createDockerClient();
//    ContainerConfig containerConfig = createContainerConfig(imageName, ports, cmd);
//    createContainer(imageName, containerConfig);
//  }
//
//  @Override
//  protected void before() throws Throwable {
//    super.before();
//    dockerClient.startContainer(container.id());
//  }
//
//  @Override
//  protected void after() {
//    super.after();
//    try {
//      dockerClient.killContainer(container.id());
//      dockerClient.removeContainer(container.id(), true);
//    } catch (DockerException | InterruptedException e) {
//      e.printStackTrace();
//    }
//    dockerClient.close();
//  }
//
//  private void createContainer(String imageName, ContainerConfig containerConfig) {
//    try {
//      dockerClient.pull(imageName);
//      container = dockerClient.createContainer(containerConfig);
//    } catch (DockerException | InterruptedException e) {
//      e.printStackTrace();
//    }
//  }
//
//  private ContainerConfig createContainerConfig(String imageName, String[] ports, String cmd) {
//    return ContainerConfig.builder().image(imageName).exposedPorts(ports).cmd(cmd).build();
//  }
//
//  /**
//   * Create a client based on DOCKER_HOST and DOCKER_CERT_PATH env vars. If not set use a default
//   * client from the spotify library.
//   */
//  private DockerClient createDockerClient() {
//    try {
//      return DefaultDockerClient.fromEnv().build();
//    } catch (DockerCertificateException e) {
//      return DefaultDockerClient.builder().build();
//    }
//  }
//}
