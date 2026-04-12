package com.example.rec

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://szfldremvmaqgkleulgg.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InN6ZmxkcmVtdm1hcWdrbGV1bGdnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU1MTY0MDIsImV4cCI6MjA5MTA5MjQwMn0.msfLD_m9uJWWTJYg-I33WnbtHSBzIDvv5ZV3nyyEjxw"
    ){
        install(Auth)
        install(Postgrest)

    }
}