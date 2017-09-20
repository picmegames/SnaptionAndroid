package com.snaptiongame.app.data.cookies

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.net.HttpCookie

class SerializableHttpCookie(@Transient private val cookie: HttpCookie) : Serializable {

    @Transient private var clientCookie: HttpCookie? = null

    fun getCookie(): HttpCookie {
        var bestCookie = cookie
        if (clientCookie != null) {
            bestCookie = clientCookie as HttpCookie
        }
        return bestCookie
    }

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeObject(cookie.name)
        out.writeObject(cookie.value)
        out.writeObject(cookie.comment)
        out.writeObject(cookie.commentURL)
        out.writeObject(cookie.domain)
        out.writeLong(cookie.maxAge)
        out.writeObject(cookie.path)
        out.writeObject(cookie.portlist)
        out.writeInt(cookie.version)
        out.writeBoolean(cookie.secure)
        out.writeBoolean(cookie.discard)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(inputStream: ObjectInputStream) {
        val name = inputStream.readObject() as String
        val value = inputStream.readObject() as String
        clientCookie = HttpCookie(name, value)
        clientCookie?.comment = inputStream.readObject() as String
        clientCookie?.commentURL = inputStream.readObject() as String
        clientCookie?.domain = inputStream.readObject() as String
        clientCookie?.maxAge = inputStream.readLong()
        clientCookie?.path = inputStream.readObject() as String
        clientCookie?.portlist = inputStream.readObject() as String
        clientCookie?.version = inputStream.readInt()
        clientCookie?.secure = inputStream.readBoolean()
        clientCookie?.discard = inputStream.readBoolean()
    }
}
