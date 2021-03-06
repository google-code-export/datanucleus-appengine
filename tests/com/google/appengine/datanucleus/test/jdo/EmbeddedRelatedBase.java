/**********************************************************************
Copyright (c) 2012 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
**********************************************************************/
package com.google.appengine.datanucleus.test.jdo;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.PersistenceCapable;

/**
 * Base of an object embedded into another object.
 */
@PersistenceCapable(embeddedOnly="true")
@Discriminator(strategy=DiscriminatorStrategy.CLASS_NAME)
public class EmbeddedRelatedBase {

  String name;

  long value;

  public EmbeddedRelatedBase(String name, long val) {
    this.name = name;
    this.value = val;
  }

  public String getName() {
    return name;
  }

  public long getValue() {
    return value;
  }
}