use crate::db::shares::create_share;
use uuid::Uuid;
#[tauri::command]
fn cmd_create_share(name: &str, path: &str, providers: &Vec<String>, last_uploaded: &str) -> String {
    let id = Uuid::new_v4();
}