package sample.utils;

import sample.domain.Candidat;

public class CandidatEvent extends Event<Candidat, EventType> {
    public CandidatEvent(Candidat candidat, EventType type) {
        super(candidat, type);
    }
}
