package be.mygod.shadowsocks.plugin.dnstt

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.github.shadowsocks.plugin.PluginOptions

class ConfigFragment : PreferenceFragmentCompat() {
    private val transport by lazy { findPreference<ListPreference>("transport")!! }
    private val resolver by lazy { findPreference<EditTextPreference>("resolver")!! }
    private val pubkey by lazy { findPreference<EditTextPreference>("pubkey")!! }
    private val domain by lazy { findPreference<EditTextPreference>("domain")!! }

    val options get() = PluginOptions().apply {
        putWithDefault(transport.value, resolver.text)
        putWithDefault("pubkey", pubkey.text)
        putWithDefault("domain", domain.text)
    }

    fun onInitializePluginOptions(options: PluginOptions) {
        val t = arrayOf("doh", "dot").firstOrNull { options[it] != null } ?: "udp"
        transport.value = t
        resolver.text = options[t]
        pubkey.text = options["pubkey"]
        domain.text = options["domain"]
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.config)
        resolver.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }
        pubkey.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            it.filters = arrayOf(InputFilter.LengthFilter(64))
        }
        domain.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(listView) { v, insets ->
            insets.apply {
                v.updatePadding(bottom = getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
            }
        }
    }
}
