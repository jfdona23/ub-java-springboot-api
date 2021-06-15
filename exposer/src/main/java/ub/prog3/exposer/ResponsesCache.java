package ub.prog3.exposer;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "responses_cache")
public class ResponsesCache {

    private @Id String address;
    private long epochTimestamp;
    @Column(length = 765)
    private String response;

    public ResponsesCache() {

        this.epochTimestamp = Instant.now().getEpochSecond();
    }

    public ResponsesCache(String address, String response) {

        this.address = address;
        this.epochTimestamp = Instant.now().getEpochSecond();
        this.response = response;
    }

    public String getAddress() {
        return address;
    }

    public long getEpochTimestamp() {
        return epochTimestamp;
    }

    public String getResponse() {
        return response;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEpochTimestamp(long epochTimestamp) {
        this.epochTimestamp = epochTimestamp;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    // @Override
    // public boolean equals(Object obj) {
    //     if (this == obj) {return true;}
    //     if (!(obj instanceof ResponsesCache)) {return false;}
    //     ResponsesCache r = (ResponsesCache) obj;
    //     return (this.epochTimestamp + 7200) >= r.epochTimestamp && Objects.equals(this.address, r.address);
    // }

    // @Override
    // public int hashCode() {
    //     return Objects.hash(this.epochTimestamp, this.address, this.response);
    // }

    // @Override
    // public String toString() {
    //     return "{timestamp= "+this.epochTimestamp+", address= "+this.address+", response= "+this.response+"}";
    // }
}
