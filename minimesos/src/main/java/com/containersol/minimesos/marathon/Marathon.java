package com.containersol.minimesos.marathon;

import com.containersol.minimesos.container.AbstractContainer;
import com.containersol.minimesos.mesos.ZooKeeper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;

/**
 * Marathon container
 */
public class Marathon extends AbstractContainer {

    private static final String MARATHON_IMAGE = "mesosphere/marathon";
    public static final String MARATHON_IMAGE_TAG = "v0.13.0";
    public static final int MARATHON_PORT = 8080;

    private ZooKeeper zooKeeper;
    private String marathonImageTag = MARATHON_IMAGE_TAG;
    private Boolean exposedHostPort;

    public Marathon(DockerClient dockerClient, ZooKeeper zooKeeper, String marathonImageTag, Boolean exposedHostPort) {
        this( dockerClient, zooKeeper, exposedHostPort);
        this.marathonImageTag = marathonImageTag;
    }

    public Marathon(DockerClient dockerClient, ZooKeeper zooKeeper, Boolean exposedHostPort) {
        super(dockerClient);
        this.zooKeeper = zooKeeper;
        this.exposedHostPort = exposedHostPort;
    }

    @Override
    protected void pullImage() {
        pullImage(MARATHON_IMAGE, marathonImageTag);
    }

    @Override
    protected CreateContainerCmd dockerCommand() {
        ExposedPort exposedPort = ExposedPort.tcp(MARATHON_PORT);
        Ports portBindings = new Ports();
        if (exposedHostPort) {
            portBindings.bind(exposedPort, Ports.Binding(MARATHON_PORT));
        }
        return dockerClient.createContainerCmd(MARATHON_IMAGE + ":" + marathonImageTag)
                .withName("minimesos-marathon-" + getClusterId() + "-" + getRandomId())
                .withExtraHosts("minimesos-zookeeper:" + this.zooKeeper.getIpAddress())
                .withCmd("--master", "zk://minimesos-zookeeper:2181/mesos", "--zk", "zk://minimesos-zookeeper:2181/marathon")
                .withExposedPorts(exposedPort)
                .withPortBindings(portBindings);
    }

}
