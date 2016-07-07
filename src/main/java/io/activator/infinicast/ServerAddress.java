package io.activator.infinicast;

import java.net.InetSocketAddress;

public class ServerAddress extends TcpEndpointAddress {
    private String address;
    public ServerAddress(String addressString) {
        super(addressString);
        this.address= addressString;
    }
    public ServerAddress(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
    }
    public ServerAddress() {
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


