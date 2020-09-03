package com.ego1st.passwordmanager

class Account (platform: String, email: String, password: String, passwordAgain: String, accessCode: Int){

    var platform: String = platform
    var email: String = email
    var password: String = password
    var passwordAgain: String =passwordAgain
    var accessCode: Int = accessCode

    var documentId: String? = null

    constructor() :this("", "", "", "", -1)
}