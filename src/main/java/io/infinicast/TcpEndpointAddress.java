package io.infinicast;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by Michael on 31.07.2015.
 */
public class TcpEndpointAddress implements IEndpointAddress, Serializable {

    SocketAddress address;

    String addressString;
    public TcpEndpointAddress(SocketAddress socketAddress) {
        address = socketAddress;
        addressString = cleanupAddress(address.toString());
    }
    public TcpEndpointAddress(){}
    /**
     * Construct an address from a describing string
     *
     * @param addressString address string like "/127.0.0.1:8080"
     */
    public TcpEndpointAddress(String addressString) {
        setAddress(addressString);
    }
    protected void setAddress(String addressString) {
        String[] parts = addressString.split(":");
        String hostname = parts[0];
        Integer port = Integer.parseInt(parts[1]);
        address = new InetSocketAddress(hostname, port);
        this.addressString = cleanupAddress(address.toString());
    }
    public SocketAddress getSocketAddress() {
        return address;
    }

    @Override
    public String getAddress() {
        return addressString;
    }

    private String cleanupAddress(String addr) {
        if(addr.contains("/")){
            if(addr.indexOf("/")==0){
                addr = addr.substring(addr.indexOf("/")+1);
            }else{

                String port = addr.substring(addr.indexOf(":"));
                addr = addr.substring(0,addr.indexOf("/"))+port;
            }
        }
        return addr;
    }

    @Override public String serializedString() {
        return getAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TcpEndpointAddress that = (TcpEndpointAddress) o;

        // We use the string representation as we dont' care for the internal state of the SocketAddress
        return getAddress().equals(that.getAddress());
    }

    @Override
    public int hashCode() {
        // We use the string representation as we dont' care for the internal state of the SocketAddress
        return getAddress().hashCode();
    }

    @Override
    public String toString() {
        return "TcpEndpointAddress{" +
                "address=" + address +
                '}';
    }
}

