package com.cafebazaar.servicebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.cafebazaar.servicebase.communicator.ClientConnectionCommunicator
import com.cafebazaar.servicebase.state.ClientError
import com.cafebazaar.servicebase.state.ClientStateListener
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val client by lazy {
        object : Client(this@MainActivity) {
            override val supportedClientVersion: Long = 1L

            override fun getServiceConnection(): ClientConnectionCommunicator? {
                return object : ClientConnectionCommunicator {
                    override fun startConnection(): Boolean {
                        return true
                    }

                    override fun stopConnection() {
                    }

                }
            }

            override fun getBroadcastConnections(): ClientConnectionCommunicator? {
                return object : ClientConnectionCommunicator {
                    override fun startConnection(): Boolean {
                        return true
                    }

                    override fun stopConnection() {
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thread {
            client.startConnection(object : ClientStateListener {
                override fun onReady() {
                    findViewById<TextView>(R.id.statusText).text = "Ready"
                }

                override fun onError(clientError: ClientError) {
                    findViewById<TextView>(R.id.statusText).text = "Error happened"
                }
            })
        }
    }
}