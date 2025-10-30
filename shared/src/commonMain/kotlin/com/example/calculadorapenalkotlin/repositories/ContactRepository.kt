package com.example.calculadorapenalkotlin.repositories
import com.russhwolf.settings.Settings

/**
 * Esta classe gerencia o armazenamento persistente
 * dos dados de contato do usuário.
 */
class ContactRepository(private val settings: Settings) {

    // Chaves de armazenamento
    companion object {
        const val KEY_NAME = "CONTACT_NAME"
        const val KEY_WHATSAPP = "CONTACT_WHATSAPP"
        const val KEY_EMAIL = "CONTACT_EMAIL"
        const val KEY_PROCESS = "CONTACT_PROCESS"
    }

    /**
     * Salva os dados de contato no armazenamento persistente.
     */
    fun saveContactInfo(name: String, whatsapp: String, email: String, process: String?) {
        settings.putString(KEY_NAME, name)
        settings.putString(KEY_WHATSAPP, whatsapp)
        settings.putString(KEY_EMAIL, email)
        if (process != null) {
            settings.putString(KEY_PROCESS, process)
        }
    }

    /**
     * Verifica se os dados de contato obrigatórios (nome e whatsapp) já foram preenchidos.
     */
    fun hasCompleteContactInfo(): Boolean {
        // Você pode definir "completo" como quiser.
        // Pelo seu MultiStepScreen, nome e whatsapp parecem ser os campos-chave.
        val name = settings.getString(KEY_NAME, "")
        val whatsapp = settings.getString(KEY_WHATSAPP, "")

        return name.isNotBlank() && whatsapp.isNotBlank()
    }

    // Você pode adicionar outras funções como getContactInfo(), clearContactInfo(), etc.
}