package com.hospital.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.add(emitter);
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        return emitter;
    }

    public void sendUpdate(Object data) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("bed-update").data(data));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}
