package io.infinicast.client.api.paths.handler.lists;
import io.infinicast.client.api.IPath;
import io.infinicast.*;
import java.util.*;

public interface IQueryResult {
    List<IPath> results();
    IPath first();
    int count();
}
