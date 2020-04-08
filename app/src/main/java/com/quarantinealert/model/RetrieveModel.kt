package com.quarantinealert.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class RetrieveModel {
    var name: String? = null
    var url: String? = null

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
       constructor() {}

    constructor(name: String?, url: String?) {
        this.name = name
        this.url = url
    }

}