package io.infinicast;

import java.net.InetSocketAddress;

public class InfinicastServerAddress extends TcpEndpointAddress {
    private String address;
    public InfinicastServerAddress(String addressString) {
        super(addressString);
        this.address= addressString;
    }
    public InfinicastServerAddress(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
    }
    public InfinicastServerAddress() {
        super();
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
        super.setAddress(this.address);
    }
}


