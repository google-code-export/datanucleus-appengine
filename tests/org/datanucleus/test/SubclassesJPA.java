/**********************************************************************
Copyright (c) 2009 Google Inc.

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
package org.datanucleus.test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * @author Max Ross <maxr@google.com>
 */
public class SubclassesJPA {

  @Entity
  @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
  public static class TablePerClass {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String aString;

    public Long getId() {
      return id;
    }

    public void setAString(String aString) {
      this.aString = aString;
    }

    public String getAString() {
      return aString;
    }
  }

  @Entity
  public static class TablePerClassChild extends TablePerClass {
    private String bString;

    public void setBString(String bString) {
      this.bString = bString;
    }

    public String getBString() {
      return bString;
    }
  }

  @Entity
  public static class TablePerClassGrandchild extends TablePerClassChild {
    private String cString;

    public void setCString(String cString) {
      this.cString = cString;
    }

    public String getCString() {
      return cString;
    }
  }

  @Entity
  @Inheritance(strategy = InheritanceType.JOINED)
  public static class Joined {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aString;

    public Long getId() {
      return id;
    }

    public void setAString(String aString) {
      this.aString = aString;
    }

    public String getAString() {
      return aString;
    }
  }

  @Entity
  public static class JoinedChild extends Joined {
    private String bString;

    public void setBString(String bString) {
      this.bString = bString;
    }

    public String getBString() {
      return bString;
    }
  }

  @Entity
  @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
  public static class SingleTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aString;

    public Long getId() {
      return id;
    }

    public void setAString(String aString) {
      this.aString = aString;
    }

    public String getAString() {
      return aString;
    }
  }

  @Entity
  public static class SingleTableChild extends SingleTable {
    private String bString;

    public void setBString(String bString) {
      this.bString = bString;
    }

    public String getBString() {
      return bString;
    }
  }

  @MappedSuperclass
  public static class MappedSuperclassParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aString;

    public Long getId() {
      return id;
    }

    public void setAString(String aString) {
      this.aString = aString;
    }

    public String getAString() {
      return aString;
    }
  }

  @Entity
  public static class MappedSuperclassChild extends MappedSuperclassParent {
    private String bString;

    public void setBString(String bString) {
      this.bString = bString;
    }

    public String getBString() {
      return bString;
    }
  }
}