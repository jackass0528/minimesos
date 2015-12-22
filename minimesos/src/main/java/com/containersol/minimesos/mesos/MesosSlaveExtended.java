package com.containersol.minimesos.mesos;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.TreeMap;

public class MesosSlaveExtended extends MesosSlave {

    private static Logger LOGGER = Logger.getLogger(MesosSlaveExtended.class);

    private final String resources;
    private final int portNumber;

    public MesosSlaveExtended(DockerClient dockerClient, boolean dockerInDocker, String resources, int portNumber, ZooKeeper zooKeeperContainer, String mesosLocalImage, String registryTag) {
        super(dockerClient, zooKeeperContainer);
        this.resources = resources;
        this.portNumber = portNumber;
        setDockerInDocker(dockerInDocker);
        setMesosImageName(mesosLocalImage);
        setMesosImageTag(registryTag);
    }

    public MesosSlaveExtended(DockerClient dockerClient, ZooKeeper zooKeeperContainer) {
        this( dockerClient,
                MesosSlave.DEFAULT_DOCKER_IN_DOCKER,
                DEFAULT_RESOURCES,
                MESOS_SLAVE_PORT,
                zooKeeperContainer,
                MESOS_SLAVE_IMAGE,
                MESOS_IMAGE_TAG);
    }

    @Override
    protected CreateContainerCmd dockerCommand() {
        ArrayList<ExposedPort> exposedPorts= new ArrayList<>();
        exposedPorts.add(new ExposedPort(this.portNumber));
        try {
            ArrayList<Integer> resourcePorts = parsePortsFromResource(this.resources);
            for (Integer port : resourcePorts) {
                exposedPorts.add(new ExposedPort(port));
            }
        } catch (Exception e) {
            LOGGER.error("Port binding is incorrect: " + e.getMessage());
        }

        CreateContainerCmd cmd = this.getBaseCommand();
        return cmd.withExposedPorts(exposedPorts.toArray(new ExposedPort[exposedPorts.size()]));
    }

    public String getResources() {
        return resources;
    }

    public String[] createMesosLocalEnvironment() {
        TreeMap<String, String> envs = getDefaultEnvVars();
        envs.putAll(getSharedEnvVars());
        envs.put("MESOS_RESOURCES", this.resources);

        return envs.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).toArray(String[]::new);
    }
}
