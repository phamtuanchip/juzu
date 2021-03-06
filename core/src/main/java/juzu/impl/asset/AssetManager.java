/*
 * Copyright 2013 eXo Platform SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package juzu.impl.asset;

import juzu.asset.AssetLocation;
import juzu.impl.common.Tools;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class AssetManager {

  /** . */
  protected final LinkedHashMap<String, AssetNode> assets = new LinkedHashMap<String, AssetNode>();

  /** . */
  protected final HashMap<String, URL> resources = new HashMap<String, URL>();

  public boolean addAsset(String id, AssetLocation location, String value, URL url, String... dependencies) throws NullPointerException, IllegalArgumentException {
    return addAsset(id, location, Collections.singletonMap(value, url), Tools.set(dependencies));
  }

  /**
   * <p>Attempt to add an asset to the manager, the manager will return the asset id
   * if the asset was registered or null if it was not.</p>
   *
   * <p>When no asset id is specified, an asset id will be generated from the asset value by taking
   * the longest trailing substring that contains no <code>/</code> char.</p>
   *
   * @param id the asset id
   * @param location the asset location
   * @param resources the asset resources
   * @param dependencies the asset dependencies
   * @return true if the asset was registered
   * @throws NullPointerException     if the metaData argument is nul
   * @throws IllegalArgumentException if the metaData does not have an id set
   */
  public boolean addAsset(String id, AssetLocation location, Map<String, URL> resources, Set<String> dependencies) throws NullPointerException, IllegalArgumentException {

    //
    if (!assets.keySet().contains(id)) {
      AssetNode asset = new AssetNode(id, location, new ArrayList<String>(resources.keySet()), dependencies);
      for (AssetNode deployed : assets.values()) {
        if (deployed.iDependOn.contains(id)) {
          asset.dependsOnMe = Tools.addToHashSet(asset.dependsOnMe, deployed.id);
        }
        if (asset.iDependOn.contains(deployed.id)) {
          deployed.dependsOnMe = Tools.addToHashSet(deployed.dependsOnMe, id);
        }
      }
      assets.put(id, asset);

      //
      for (Map.Entry<String, URL> resource : resources.entrySet()) {
        if (resource.getValue() != null) {
          this.resources.put(resource.getKey(), resource.getValue());
        }
      }

      //
      return true;
    } else {
      // log it ?
      return false;
    }
  }

  /**
   * Resolve an asset as a resource URL or return null if it cannot be found.
   *
   * @param path the path
   * @return the resource
   */
  public URL resolveAsset(String path) {
    return resources.get(path);
  }

  /**
   * Perform a topological sort of the provided asset script values.
   *
   * @param ids the asset ids to resolve
   * @return the resolved asset or null
   * @throws NullPointerException if the asset ids argument is null
   * @throws IllegalArgumentException when asset dependencies cannot be resolved
   */
  public Iterable<Asset> resolveAssets(Iterable<String> ids) throws
    NullPointerException,
    IllegalArgumentException {

    // Compute the closure
    LinkedHashMap<String, HashSet<String>> sub = new LinkedHashMap<String, HashSet<String>>();
    for (LinkedList<String> queue = Tools.addAll(new LinkedList<String>(), ids);!queue.isEmpty();) {
      String id = queue.removeFirst();
      AssetNode asset = this.assets.get(id);
      if (asset != null) {
        sub.put(asset.id, new HashSet<String>(asset.iDependOn));
        for (String depend : asset.iDependOn) {
          if (!sub.containsKey(depend)) {
            queue.addLast(depend);
          }
        }
      }
      else {
        throw new IllegalArgumentException("Cannot resolve asset " + id);
      }
    }

    // Perform the topological sort
    LinkedList<Asset> resolved = new LinkedList<Asset>();
    while (sub.size() > 0) {
      boolean found = false;
      for (Iterator<Map.Entry<String, HashSet<String>>> i = sub.entrySet().iterator();i.hasNext();) {
        Map.Entry<String, HashSet<String>> entry = i.next();
        if (entry.getValue().isEmpty()) {
          i.remove();
          AssetNode asset = this.assets.get(entry.getKey());
          resolved.addAll(asset.assets);
          for (String dependency : asset.dependsOnMe) {
            HashSet<String> foo = sub.get(dependency);
            if (foo != null) {
              foo.remove(entry.getKey());
            }
          }
          found = true;
          break;
        }
      }
      if (!found) {
        StringBuilder sb = new StringBuilder("Cannot satisfy asset dependencies:\n");
        for (Map.Entry<String, HashSet<String>> entry : sub.entrySet()) {
          sb.append(entry.getKey()).append(" -> ").append(entry.getValue());
        }
        throw new IllegalArgumentException(sb.toString());
      }
    }

    //
    return resolved;
  }
}
