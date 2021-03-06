/**********************************************************************
Copyright (c) 2011 Google Inc.

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
package com.google.appengine.datanucleus.jpa;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.StorageVersion;
import com.google.appengine.datanucleus.Utils;
import com.google.appengine.datanucleus.test.jpa.UnownedUpgradeJPA.HasOneToManyWithKey;
import com.google.appengine.datanucleus.test.jpa.UnownedUpgradeJPA.HasOneToManyWithUnowned;
import com.google.appengine.datanucleus.test.jpa.UnownedUpgradeJPA.HasOneToOneWithKey;
import com.google.appengine.datanucleus.test.jpa.UnownedUpgradeJPA.HasOneToOneWithUnowned;
import com.google.appengine.datanucleus.test.jpa.UnownedUpgradeJPA.SideB;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;

import java.util.Map;

/**
 * Tests for upgrading Key relations to unowned relations
 */
public class JPAUnownedUpgradeTest extends JPATestCase {

  @Override
  protected String getAppId() {
    return "s~DNTest";
  }

  @Override
  protected LocalDatastoreServiceTestConfig newLocalDatastoreServiceTestConfig() {
    LocalDatastoreServiceTestConfig config = super.newLocalDatastoreServiceTestConfig();
    return config.setDefaultHighRepJobPolicyUnappliedJobPercentage(1);
  }

  public void testOneToManyUpgrade() {
    switchDatasource(EntityManagerFactoryName.originalStorageVersion);

    // Do the writes with the relationship managed via Key
    SideB sideB1 = new SideB();
    sideB1.setName("yar");
    beginTxn();
    em.persist(sideB1);
    commitTxn();

    SideB sideB2 = new SideB();
    sideB2.setName("bar");
    beginTxn();
    em.persist(sideB2);
    commitTxn();

    HasOneToManyWithKey withKey = new HasOneToManyWithKey();
    withKey.addOther(KeyFactory.createKey("UnownedUpgradeJPA$SideB", sideB1.getId()));
    withKey.addOther(KeyFactory.createKey("UnownedUpgradeJPA$SideB", sideB2.getId()));
    beginTxn();
    em.persist(withKey);
    commitTxn();

    beginTxn();
    assertEquals(2, em.find(
        HasOneToManyWithKey.class, withKey.getId()).getOthers().size());
    commitTxn();

    Map<String, String> props = Utils.newHashMap();
    props.put("datanucleus.appengine.datastoreEnableXGTransactions", Boolean.TRUE.toString());
    props.put("datanucleus.appengine.storageVersion", StorageVersion.PARENTS_DO_NOT_REFER_TO_CHILDREN.name());
    switchDatasource(EntityManagerFactoryName.originalStorageVersion, props);
    // Now read it back as an unowned relationship
    beginTxn();
    HasOneToManyWithUnowned withUnowned =
        em.find(HasOneToManyWithUnowned.class, withKey.getId());
    assertEquals(2, withUnowned.getOthers().size());
    assertEquals(sideB1.getId(), withUnowned.getOthers().get(0).getId());
    assertEquals(sideB2.getId(), withUnowned.getOthers().get(1).getId());
    commitTxn();
  }

  public void testOneToOneUpgrade() throws EntityNotFoundException {
    switchDatasource(EntityManagerFactoryName.originalStorageVersion);

    // Do the writes with the relationship managed via Key
    SideB sideB1 = new SideB();
    sideB1.setName("yar");
    beginTxn();
    em.persist(sideB1);
    commitTxn();

    HasOneToOneWithKey withKey = new HasOneToOneWithKey();
    withKey.setOther(KeyFactory.createKey("UnownedUpgradeJPA$SideB", sideB1.getId()));
    beginTxn();
    em.persist(withKey);
    commitTxn();

    beginTxn();
    assertNotNull(em.find(HasOneToOneWithKey.class, withKey.getId()).getOther());
    commitTxn();

    Map<String, String> props = Utils.newHashMap();
    props.put("datanucleus.appengine.datastoreEnableXGTransactions", Boolean.TRUE.toString());
    props.put("datanucleus.appengine.storageVersion", StorageVersion.PARENTS_DO_NOT_REFER_TO_CHILDREN.name());
    switchDatasource(EntityManagerFactoryName.originalStorageVersion, props);

    // Now read it back as an unowned relationship
    beginTxn();
    HasOneToOneWithUnowned withUnowned = em.find(HasOneToOneWithUnowned.class, withKey.getId());
    assertNotNull(withUnowned.getOther());
    assertEquals(sideB1.getId(), withUnowned.getOther().getId());
    commitTxn();
  }
}