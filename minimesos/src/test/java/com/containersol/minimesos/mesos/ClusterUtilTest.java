package com.containersol.minimesos.mesos;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests helper methods
 */
public class ClusterUtilTest {
    @Test
    public void shouldBeAbleToAddSlaves() {
        ClusterArchitecture.Builder builder = ClusterUtil.withSlaves(3);
        assertEquals(3 + 1 + 1, builder.build().getClusterContainers().getContainers().size());
    }

    @Test
    public void shouldBeAbleToAddCustomSlaves() {
        MesosSlave mock = mock(MesosSlave.class);
        ClusterArchitecture.Builder builder = ClusterUtil.withSlaves(3, zk -> mock);
        assertEquals(3 + 1 + 1, builder.build().getClusterContainers().getContainers().size());
    }
}