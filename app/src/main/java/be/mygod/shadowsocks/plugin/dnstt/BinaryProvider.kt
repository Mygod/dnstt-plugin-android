package be.mygod.shadowsocks.plugin.dnstt

import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import com.github.shadowsocks.plugin.NativePluginProvider
import com.github.shadowsocks.plugin.PathProvider
import java.io.File
import java.io.FileNotFoundException

class BinaryProvider : NativePluginProvider() {
    override fun populateFiles(provider: PathProvider) {
        provider.addPath("dnstt", 0b111101101)
    }
    override fun getExecutable() = context!!.applicationInfo.nativeLibraryDir + "/libdnstt.so"
    override fun openFile(uri: Uri): ParcelFileDescriptor = when (uri.path) {
        "/dnstt" -> ParcelFileDescriptor.open(File(getExecutable()), ParcelFileDescriptor.MODE_READ_ONLY)
        else -> throw FileNotFoundException()
    }
}
