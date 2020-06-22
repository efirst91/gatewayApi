package com.bulgaria.musalasoft;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.bulgaria.musalasoft.controller.GatewayControllerTest;
import com.bulgaria.musalasoft.controller.PeripheralDControllerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ GatewayControllerTest.class, PeripheralDControllerTest.class })
public class GatewayApiApplicationTest {

}
