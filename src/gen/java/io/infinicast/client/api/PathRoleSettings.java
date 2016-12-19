package io.infinicast.client.api;

import io.infinicast.JObject;
public class PathRoleSettings {
    public Boolean sendMessage;
    public Boolean receiveMessage;
    public Boolean validateMessage;
    public Boolean requiresMessageValidation;
    public Boolean readMessageListenerList;
    public Boolean sendRequest;
    public Boolean answerRequest;
    public Boolean readRequestListenerList;
    public Boolean writeData;
    public Boolean readData;
    public Boolean validateData;
    public Boolean requiresDataValidation;
    public Boolean readDataListenerList;
    public PathRoleSettings allowAllMessage() {
        this.sendMessage = true;
        this.receiveMessage = true;
        this.validateMessage = true;
        this.readMessageListenerList = true;
        return this;
    }
    public PathRoleSettings allowAllRequest() {
        this.sendRequest = true;
        this.answerRequest = true;
        this.readRequestListenerList = true;
        return this;
    }
    public PathRoleSettings allowAllData() {
        this.writeData = true;
        this.readData = true;
        this.validateData = true;
        this.readDataListenerList = true;
        return this;
    }
    public PathRoleSettings allowAll() {
        this.allowAllMessage();
        this.allowAllRequest();
        this.allowAllData();
        return this;
    }
    public PathRoleSettings denyAllMessage() {
        this.sendMessage = false;
        this.receiveMessage = false;
        this.validateMessage = false;
        this.readMessageListenerList = false;
        return this;
    }
    public PathRoleSettings denyAllRequest() {
        this.sendRequest = false;
        this.answerRequest = false;
        this.readRequestListenerList = false;
        return this;
    }
    public PathRoleSettings denyAllData() {
        this.writeData = false;
        this.readData = false;
        this.validateData = false;
        this.readDataListenerList = false;
        return this;
    }
    public PathRoleSettings allowAllListenerLists() {
        this.readMessageListenerList = true;
        this.readDataListenerList = true;
        this.readRequestListenerList = true;
        return this;
    }
    public PathRoleSettings denyAllListenerLists() {
        this.readMessageListenerList = false;
        this.readDataListenerList = false;
        this.readRequestListenerList = false;
        return this;
    }
    public PathRoleSettings denyAll() {
        this.denyAllMessage();
        this.denyAllRequest();
        this.denyAllData();
        return this;
    }
    public JObject toJson() {
        JObject data = new JObject();
        if (this.sendMessage != null) {
            data.set("sendMessage", this.sendMessage);
        }
        if (this.receiveMessage != null) {
            data.set("receiveMessage", this.receiveMessage);
        }
        if (this.validateMessage != null) {
            data.set("validateMessage", this.validateMessage);
        }
        if (this.requiresMessageValidation != null) {
            data.set("requiresMessageValidation", this.requiresMessageValidation);
        }
        if (this.readMessageListenerList != null) {
            data.set("readMessageListenerList", this.readMessageListenerList);
        }
        if (this.sendRequest != null) {
            data.set("sendRequest", this.sendRequest);
        }
        if (this.answerRequest != null) {
            data.set("answerRequest", this.answerRequest);
        }
        if (this.readRequestListenerList != null) {
            data.set("readRequestListenerList", this.readRequestListenerList);
        }
        if (this.writeData != null) {
            data.set("writeData", this.writeData);
        }
        if (this.readData != null) {
            data.set("readData", this.readData);
        }
        if (this.validateData != null) {
            data.set("validateData", this.validateData);
        }
        if (this.requiresDataValidation != null) {
            data.set("requiresDataValidation", this.requiresDataValidation);
        }
        if (this.readDataListenerList != null) {
            data.set("readDataListenerList", this.readDataListenerList);
        }
        return data;
    }
}
