package io.infinicast.client.api.errors;

public class ICException extends Exception  {
    ICError _error;
    public ICException(ICError error) {
        super(error.toString());
        this.setError(error);
    }
    public String toString() {
        return super.getMessage();
    }
    public ICError getError() {
        return this._error;
    }
    public void setError(ICError value) {
        this._error = value;
    }
}
