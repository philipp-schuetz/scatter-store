use diesel::sqlite::SqliteConnection;
use diesel::prelude::*;

pub mod models;
pub mod schema;
pub fn establish_connection() -> SqliteConnection {

    let database_url = "scatter-store.sqlite";
    SqliteConnection::establish(&database_url)
        .unwrap_or_else(|_| panic!("Error connecting to {}", database_url))
}