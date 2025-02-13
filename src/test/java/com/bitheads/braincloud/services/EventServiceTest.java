package com.bitheads.braincloud.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.bitheads.braincloud.client.IEventCallback;

public class EventServiceTest extends TestFixtureBase implements IEventCallback {
    private final String _eventType = "test";
    private final String _eventDataKey = "testData";
    private final String _eventData = "testEventData";

    private boolean _callbackRan = false;
    private String _eventId = null;
    private String _nonExistentEventId = "66ba5285d9002730d8f707a0";
    private String[] evIds = {"608c40c3a3a7950f388c4eac", "368c40c3a3a7950f388c4eac"};

    private String eventType = "my-event_type";
    private long dateMillis = 1619804426154L;

    @After
    public void Teardown() throws Exception {
        if (_eventId != null && !_eventId.isEmpty()) {
        cleanupIncomingEvent(_eventId);
        }
    }

    @Test
    public void testSendEvent() throws Exception {
        _wrapper.getClient().registerEventCallback(this);

        sendDefaultMessage();

        Assert.assertTrue(_callbackRan);

        _wrapper.getClient().deregisterEventCallback();
    }

    @Test
    public void testSendEventToProfiles() throws Exception{
        TestResult tr = new TestResult(_wrapper);
        String profileId = _wrapper.getStoredProfileId();
        JSONArray toIdsList = new JSONArray();
        toIdsList.put(profileId);
        String toIds = toIdsList.toString();

        _wrapper.getEventService().sendEventToProfiles(
                toIds,
                _eventType,
                Helpers.createJsonPair(_eventDataKey, _eventData),
                tr);

        tr.Run();
    }

    @Override
    public void eventsReceived(JSONObject events) {
        //Console.WriteLine("Events received: " + jsonResponse);
      //   int numEvents = 0;
        try {
            // numEvents = events.getJSONArray("events").length();
            events.getJSONArray("events").length();
        } catch (JSONException je) {
            je.printStackTrace();
        }

        //Assert.assertEquals(numEvents, 1);
        _callbackRan = true;
    }

    @Test
    public void testUpdateIncomingEventData() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        sendDefaultMessage();

        _wrapper.getEventService().updateIncomingEventData(
                _eventId,
                Helpers.createJsonPair(_eventDataKey, _eventData),
                tr);

        tr.Run();
    }

    @Test
    public void testUpdateIncomingEventDataIfExistsTrue() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        sendDefaultMessage();

        _wrapper.getEventService().updateIncomingEventDataIfExists(
                _eventId,
                Helpers.createJsonPair(_eventDataKey, _eventData),
                tr);

        tr.Run();
    }

    @Test
    public void testUpdateIncomingEventDataIfExistsFalse() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getEventService().updateIncomingEventDataIfExists(_nonExistentEventId, Helpers.createJsonPair(_eventDataKey, _eventData), tr);
        tr.Run();
    }

    @Test
    public void testDeleteIncomingEvent() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        sendDefaultMessage();

        _wrapper.getEventService().deleteIncomingEvent(
                _eventId,
                tr);

        tr.Run();
        _eventId = null;
    }

    @Test
    public void testDeleteIncomingEvents() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        sendDefaultMessage();

        _wrapper.getEventService().deleteIncomingEvents(evIds, tr);

        tr.Run();
        evIds = null;
    }

    @Test
    public void testDeleteIncomingEventsByTypeOlderThan() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getEventService().deleteIncomingEventsByTypeOlderThan(eventType, dateMillis, tr);

        tr.Run();
    }

    @Test
    public void testDeleteIncomingEventsOlderThan() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getEventService().deleteIncomingEventsOlderThan(dateMillis, tr);

        tr.Run();
    }

    @Test
    public void testGetEvents() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        sendDefaultMessage();

        _wrapper.getEventService().getEvents(tr);

        tr.Run();
    }

    /// helpers

    private void sendDefaultMessage() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getEventService().sendEvent(
                _wrapper.getStoredProfileId(),
                _eventType,
                Helpers.createJsonPair(_eventDataKey, _eventData),
                tr);

        if (tr.Run()) {
            _eventId = tr.m_response.getJSONObject("data").getString("evId");
        }
    }

    private void cleanupIncomingEvent(String eventId) throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getEventService().deleteIncomingEvent(
                eventId,
                tr);

        tr.Run();
    }
}