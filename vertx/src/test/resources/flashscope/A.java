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

package flashscope;

import juzu.Action;
import juzu.Response;
import juzu.Route;
import juzu.View;
import juzu.bridge.vertx.FlashScopeTestCase;

import javax.inject.Inject;
import java.util.UUID;

public class A {

  @Inject
  Flash flash;

  String flash() {
    if (flash.value == null) {
      flash.value = UUID.randomUUID().toString();
    }
    return flash.value;
  }

  @View
  public Response.Content index() {
    FlashScopeTestCase.RENDER = flash();
    return Response.ok("pass");
  }

  @Action
  @Route("/action")
  public Response.View action() {
    FlashScopeTestCase.ACTION = flash();
    return A_.index();
  }
}