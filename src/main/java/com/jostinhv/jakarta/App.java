package com.jostinhv.jakarta;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

/**
 * @author JostinHv
 */
@ApplicationPath("/api")
@LoginConfig(authMethod = "MP-JWT", realmName = "MP JWT Realm")
public class App extends Application {
}
