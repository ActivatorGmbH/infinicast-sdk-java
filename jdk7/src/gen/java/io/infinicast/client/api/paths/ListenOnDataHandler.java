package io.infinicast.client.api.paths;

import io.infinicast.BiConsumer;
import io.infinicast.JObject;
import io.infinicast.client.api.IPath;
public class ListenOnDataHandler implements IListenOnDataHandler {
    BiConsumer<JObject, IPath> _onAdd;
    BiConsumer<JObject, IPath> _onChange;
    BiConsumer<JObject, IPath> _onRemove;
    public ListenOnDataHandler() {
        this._onAdd = null;
        this._onChange = null;
        this._onRemove = null;
    }
    public ListenOnDataHandler withAddHandler(BiConsumer<JObject, IPath> onAdd) {
        this._onAdd = onAdd;
        return this;
    }
    public ListenOnDataHandler withChangeHandler(BiConsumer<JObject, IPath> onChange) {
        this._onChange = onChange;
        return this;
    }
    public ListenOnDataHandler withRemoveHandler(BiConsumer<JObject, IPath> onRemove) {
        this._onRemove = onRemove;
        return this;
    }
    public void onAdd(JObject data, IPath context) {
        if (this._onAdd != null) {
            this._onAdd.accept(data, context);
            ;
        }
    }
    public void onChange(JObject data, IPath context) {
        if (this._onChange != null) {
            this._onChange.accept(data, context);
            ;
        }
    }
    public void onRemove(JObject data, IPath context) {
        if (this._onRemove != null) {
            this._onRemove.accept(data, context);
            ;
        }
    }
}
