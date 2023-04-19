package dataBase

import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session
import java.io.IOException

fun startSession(uri: String, user: String, password: String): Session {
    try {
        val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
        return driver.session()
    } catch (e: IllegalArgumentException) {
        throw IOException("can't start session, try to chainge uri, user name or password")
    }
}

fun closeSession(session: Session, driver: Driver) {
    session.close()
    driver.close()
}

fun cleanDB(session: Session) {
    session.run("MATCH (n) DETACH DELETE n")
}

fun cleanAndClose(session: Session, driver: Driver) {
    cleanDB(session)
    closeSession(session, driver)
}