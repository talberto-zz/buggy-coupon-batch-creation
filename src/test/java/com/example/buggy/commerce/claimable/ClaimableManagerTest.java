package com.example.buggy.commerce.claimable;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.commerce.CommerceException;
import atg.commerce.claimable.ClaimableManager;
import atg.nucleus.Nucleus;
import atg.nucleus.NucleusTestUtils;
import atg.nucleus.ServiceException;
import atg.repository.RepositoryException;
import atg.repository.RepositoryImpl;
import atg.repository.RepositoryItem;

public class ClaimableManagerTest  {
  private static final String BATCH_PROMOTION_CLAIMABLE_ID = "GPR0JYF1HS";
  
  Nucleus nucleus;
  ClaimableManager claimableManager;
  RepositoryImpl claimableRepository;
  final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Before public void setUp() throws Exception {
    logger.info("Starting Nucleus.");
    try {
      nucleus = NucleusTestUtils.startNucleusWithModules(new String[] { "DCS" }, this.getClass(), "");
      assertThat("nucleus is null", nucleus, is(notNullValue()));
      claimableManager = (ClaimableManager) nucleus.resolveName("/atg/commerce/claimable/ClaimableManager");
      assertThat("claimableManager is null", claimableManager, is(notNullValue()));
      claimableRepository = (RepositoryImpl) nucleus.resolveName("/atg/commerce/claimable/ClaimableRepository");
      assertThat("claimableRepository is null", claimableRepository, is(notNullValue()));
    } catch (ServletException e) {
      fail(e.getMessage());
    }
  }
  
  @After public void tearDown() {
    logger.info("Stopping Nucleus");
    if (nucleus != null) {
      try {
        NucleusTestUtils.shutdownNucleus(nucleus);
      } catch (ServiceException e) {
        fail(e.getMessage());
      } catch (IOException e) {
        fail(e.getMessage());
      }
    }
  }
  
  @Test public void createBatchPromotionClaimable() throws RepositoryException, CommerceException {
    RepositoryItem batchPromotionClaimable;

    // Call the method that we want to test
    logger.info("Calling ClaimableManager.findAndClaimCoupon({})", BATCH_PROMOTION_CLAIMABLE_ID);
    batchPromotionClaimable = claimableManager.findAndClaimCoupon(BATCH_PROMOTION_CLAIMABLE_ID);

    // Clear repository cache
    logger.info("Invalidating cache for repository {}", claimableRepository);
    claimableRepository.invalidateCaches();
    
    logger.info("Checking promotions property of newly created BatchPromotionClaimable");
    batchPromotionClaimable = claimableRepository.getItem(BATCH_PROMOTION_CLAIMABLE_ID, "BatchPromotionClaimable");
    assertThat("batchPromotionClaimable is null", batchPromotionClaimable, is(notNullValue()));
    
    Object promotionsObj = batchPromotionClaimable.getPropertyValue("promotions");
    assertThat("batchPromotionClaimable.promotions is null", promotionsObj, is(notNullValue()));
    assertThat("batchPromotionClaimable.promotions is a set", promotionsObj, is(instanceOf(Set.class)));
    @SuppressWarnings("unchecked")
    Set<RepositoryItem> promotions = (Set<RepositoryItem>) promotionsObj;
    assertThat("batchPromotionClaimable.promotions incorrect size", promotions, hasSize(1));
  }
}
