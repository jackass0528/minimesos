package com.containersol.minimesos.main;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.containersol.minimesos.marathon.Marathon;
import com.containersol.minimesos.mesos.MesosContainer;
import com.containersol.minimesos.mesos.ZooKeeper;

/**
 * Parameters for the 'up' command
 */
@Parameters(separators = "=", commandDescription = "Create a minimesos cluster")
public class CommandUp {

    @Parameter(names = "--exposedHostPorts", description = "Expose the Mesos and Marathon UI ports on the host level (we recommend to enable this on Mac (e.g. when using docker-machine) and disable on Linux).")
    private boolean exposedHostPorts = false;

    @Parameter(names = "--marathonImageTag", description = "The tag of the Marathon Docker image.")
    private String marathonImageTag = Marathon.MARATHON_IMAGE_TAG;

    @Parameter(names = "--mesosImageTag", description = "The tag of the Mesos master and agent Docker images.")
    private String mesosImageTag = MesosContainer.MESOS_IMAGE_TAG;

    @Parameter(names = "--zooKeeperImageTag", description = "The tag of the ZooKeeper Docker images.")
    private String zooKeeperImageTag = ZooKeeper.ZOOKEEPER_IMAGE_TAG;

    public String getMarathonImageTag() {
        return marathonImageTag;
    }

    @Parameter(names = "--timeout", description = "Time to wait for a container to get responsive, in seconds.")
    private int timeout = MesosContainer.DEFAULT_TIMEOUT_SEC;

    public String getMesosImageTag() {
        return mesosImageTag;
    }

    public String getZooKeeperImageTag() {
        return zooKeeperImageTag;
    }

    public boolean isExposedHostPorts() {
        return exposedHostPorts;
    }

    public int getTimeout() {
        return timeout;
    }
}
