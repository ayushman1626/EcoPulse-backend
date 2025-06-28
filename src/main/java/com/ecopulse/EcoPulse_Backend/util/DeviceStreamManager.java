package com.ecopulse.EcoPulse_Backend.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// This class manages Server-Sent Events (SSE) for device data streams.
@Component
public class DeviceStreamManager {

    private final Map<UUID, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public void register(UUID deviceId, SseEmitter emitter) {
        emitters.computeIfAbsent(deviceId, id -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(deviceId, emitter));
        emitter.onTimeout(() -> removeEmitter(deviceId, emitter));
        emitter.onError((ex) -> removeEmitter(deviceId, emitter));
    }

    private void removeEmitter(UUID deviceId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(deviceId);
        if (list != null) {
            list.remove(emitter);
            if (list.isEmpty()) {
                emitters.remove(deviceId);
            }
        }
    }

    public void broadcast(UUID deviceId, Object data) {
        List<SseEmitter> list = emitters.get(deviceId);
        if (list == null) return;

        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : list) {
            try {
                emitter.send(SseEmitter.event().name("device-live").data(data));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        }

        // Remove any dead connections
        list.removeAll(deadEmitters);
    }
}
