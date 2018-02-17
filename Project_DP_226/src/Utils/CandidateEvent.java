package Utils;

import Entites.Candidate;

public class CandidateEvent extends Event<EventType, Candidate> {
    public CandidateEvent(EventType eventType, Candidate data) {
        super(eventType, data);
    }
}
