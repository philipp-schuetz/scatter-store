use diesel::prelude::*;

#[derive(Queryable, Selectable)]
#[diesel(table_name = crate::schema::share)]
#[diesel(check_for_backend(diesel::sqlite::Sqlite))]
pub struct Share {
    pub uuid: String,
    pub name: String,
    pub path: String,
    pub last_uploaded: String,
    pub providers: Vec<String>,
}