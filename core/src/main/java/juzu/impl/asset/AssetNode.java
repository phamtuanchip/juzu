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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class AssetNode {

  /** . */
  private static final Set<String> EMPTY_SET = Collections.emptySet();

  /** . */
  final String id;

  /** . */
  final AssetLocation location;

  /** . */
  final List<String> value;

  /** . */
  final List<Asset> assets;

  /** . */
  Set<String> dependsOnMe;

  /** . */
  Set<String> iDependOn;

  public AssetNode(String id, AssetLocation location, List<String> value, Set<String> iDependOn) {

    List<Asset> assets = new ArrayList<Asset>(value.size());
    for (String v : value) {
      assets.add(Asset.of(location, v));
    }

    //
    this.id = id;
    this.location = location;
    this.value = value;
    this.dependsOnMe = EMPTY_SET;
    this.iDependOn = iDependOn;
    this.assets = assets;
  }

  public String getId() {
    return id;
  }

  public AssetLocation getLocation() {
    return location;
  }

  public List<String> getValue() {
    return value;
  }
}
