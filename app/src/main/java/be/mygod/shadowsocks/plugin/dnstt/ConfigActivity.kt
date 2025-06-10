package be.mygod.shadowsocks.plugin.dnstt

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.github.shadowsocks.plugin.ConfigurationActivity
import com.github.shadowsocks.plugin.PluginOptions

class ConfigActivity : ConfigurationActivity(), Toolbar.OnMenuItemClickListener {
    private val child by lazy { supportFragmentManager.findFragmentById(R.id.content) as ConfigFragment }
    private lateinit var oldOptions: PluginOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(statusBarInsets.left, statusBarInsets.top, statusBarInsets.right, statusBarInsets.bottom)
            WindowInsetsCompat.Builder(insets).apply {
                setInsets(WindowInsetsCompat.Type.statusBars(), Insets.NONE)
            }.build()
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        findViewById<Toolbar>(com.github.shadowsocks.plugin.R.id.toolbar).apply {
            title = this@ConfigActivity.title
            setNavigationIcon(com.github.shadowsocks.plugin.R.drawable.ic_navigation_close)
            setNavigationOnClickListener { onBackPressed() }
            inflateMenu(R.menu.toolbar_config)
            setOnMenuItemClickListener(this@ConfigActivity)
        }
    }

    override fun onInitializePluginOptions(options: PluginOptions) {
        oldOptions = options
        child.onInitializePluginOptions(options)
    }

    override fun onMenuItemClick(item: MenuItem?) = when (item?.itemId) {
        R.id.action_apply -> {
            saveChanges(child.options)
            finish()
            true
        }
        else -> false
    }

    override fun onBackPressed() {
        if (child.options != oldOptions) AlertDialog.Builder(this).run {
            setTitle(com.github.shadowsocks.plugin.R.string.unsaved_changes_prompt)
            setPositiveButton(com.github.shadowsocks.plugin.R.string.yes) { _, _ ->
                saveChanges(child.options)
                finish()
            }
            setNegativeButton(com.github.shadowsocks.plugin.R.string.no) { _, _ -> finish() }
            setNeutralButton(android.R.string.cancel, null)
            create()
        }.show() else super.onBackPressed()
    }
}
