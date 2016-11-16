package io.infinicast;

import java.io.Serializable;

public class LowlevelIntroductionMessage implements Serializable {
    private String addressString;
    public LowlevelIntroductionMessage(InfinicastServerAddress address) {
        this.addressString = address.getAddress();
    }
    public LowlevelIntroductionMessage(IEndpointAddress address) {
        this.addressString = address.getAddress();
    }
    public LowlevelIntroductionMessage(String addressString) {
        this.addressString = addressString;
    }
    public String getAddressString() {
        return addressString;
    }
}

