/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the CacheCollector.
 *
 * @author carcassi
 */
public class BasicTypeSupportTest {
    
    @BeforeClass
    public static void installSupport() {
        BasicTypeSupport.install();
    }

    @Test
    public void listSupport1() {
        List<Object> oldValues = null;
        List<Object> newValues = new ArrayList<>();
        newValues.add("this");
        Notification<List<Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(true));
        assertThat(notification.getNewValue(), equalTo(newValues));
        assertThat(notification.getNewValue(), not(sameInstance(newValues)));
    }

    @Test
    public void listSupport2() {
        List<Object> oldValues = new ArrayList<>();
        List<Object> newValues = new ArrayList<>();
        Notification<List<Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(false));
    }

    @Test
    public void listSupport3() {
        List<Object> oldValues = new ArrayList<>();
        oldValues.add("This");
        List<Object> newValues = new ArrayList<>();
        newValues.add("That");
        Notification<List<Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(true));
        assertThat(notification.getNewValue(), equalTo(newValues));
        assertThat(notification.getNewValue(), not(sameInstance(newValues)));
        assertThat(notification.getNewValue(), not(sameInstance(oldValues)));
    }

    @Test
    public void listSupport4() {
        List<Object> oldValues = new ArrayList<>();
        oldValues.add("This");
        List<Object> newValues = new ArrayList<>();
        newValues.add("This");
        Notification<List<Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(false));
    }
    
}
