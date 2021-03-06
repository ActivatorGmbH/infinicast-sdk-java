package io.infinicast.client.protocol.messages;

import io.infinicast.JObject;
import io.infinicast.StringExtensions;
public class BaseMessage {
    String _role;
    String _path;
    JObject _data;
    String _originalEndpoint;
    Integer _requestId;
    String _answerTarget;
    String _endpoint;
    public void _setDataByMessage(JObject data) {
        if (data.get("role") != null) {
            this.setRole(data.getString("role"));
        }
        if (data.get("path") != null) {
            this.setPath(data.getString("path"));
        }
        if (data.get("data") != null) {
            this.setData(data.getJObject("data"));
        }
        if (data.get("originalEndpoint") != null) {
            this.setOriginalEndpoint(data.getString("originalEndpoint"));
        }
        if (data.get("requestId") != null) {
            this.setRequestId(data.getInt("requestId"));
        }
        else {
            this.setRequestId(null);
        }
        if (data.get("answerTarget") != null) {
            this.setAnswerTarget(data.getString("answerTarget"));
        }
        if (data.get("endpoint") != null) {
            this.setEndpoint(data.getString("endpoint"));
        }
    }
    public void _setDataByClone(BaseMessage original) {
        this.setRole(original.getRole());
        this.setPath(original.getPath());
        this.setData(original.getData());
        this.setOriginalEndpoint(original.getOriginalEndpoint());
        this.setRequestId(original.getRequestId());
        this.setAnswerTarget(original.getAnswerTarget());
        this.setEndpoint(original.getEndpoint());
    }
    public void _fillJson(JObject result) {
        if (!(StringExtensions.IsNullOrEmpty(this.getRole()))) {
            result.set("role", this.getRole());
        }
        if (!(StringExtensions.IsNullOrEmpty(this.getPath()))) {
            result.set("path", this.getPath());
        }
        if (this.getData() != null) {
            result.set("data", this.getData());
        }
        if (!(StringExtensions.IsNullOrEmpty(this.getOriginalEndpoint()))) {
            result.set("originalEndpoint", this.getOriginalEndpoint());
        }
        if (this.getRequestId() != null) {
            result.set("requestId", this.getRequestId());
        }
        if (!(StringExtensions.IsNullOrEmpty(this.getAnswerTarget()))) {
            result.set("answerTarget", this.getAnswerTarget());
        }
        if (!(StringExtensions.IsNullOrEmpty(this.getEndpoint()))) {
            result.set("endpoint", this.getEndpoint());
        }
    }
    public String getRole() {
        return this._role;
    }
    public void setRole(String value) {
        this._role = value;
    }
    public String getPath() {
        return this._path;
    }
    public void setPath(String value) {
        this._path = value;
    }
    public JObject getData() {
        return this._data;
    }
    public void setData(JObject value) {
        this._data = value;
    }
    public String getOriginalEndpoint() {
        return this._originalEndpoint;
    }
    public void setOriginalEndpoint(String value) {
        this._originalEndpoint = value;
    }
    public Integer getRequestId() {
        return this._requestId;
    }
    public void setRequestId(Integer value) {
        this._requestId = value;
    }
    public String getAnswerTarget() {
        return this._answerTarget;
    }
    public void setAnswerTarget(String value) {
        this._answerTarget = value;
    }
    public String getEndpoint() {
        return this._endpoint;
    }
    public void setEndpoint(String value) {
        this._endpoint = value;
    }
}
