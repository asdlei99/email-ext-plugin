import hudson.plugins.emailext.plugins.EmailTrigger

// Namespaces
l = namespace("/lib/layout")
st = namespace("jelly:stapler")
j = namespace("jelly:core")
t = namespace("/lib/hudson")
f = namespace("/lib/form")
d = namespace("jelly:define")


def triggers = hudson.plugins.emailext.plugins.EmailTrigger.all()
def configured = instance != null ? instance.configured : false

f.entry(title: _("Project Recipient List"), help: "/plugin/email-ext/help/projectConfig/globalRecipientList.html", description: _("Comma-separated list of email address that should receive notifications for this project.")) {
  f.textbox(name: "project_recipient_list", value: configured ? instance.recipientList : "\$DEFAULT_RECIPIENTS", checkUrl: "'${rootURL}/publisher/ExtendedEmailPublisher/recipientListRecipientsCheck?value='+encodeURIComponent(this.value)") 
}
f.entry(title: _("Project Reply-To List"), help: "/plugin/email-ext/help/projectConfig/replyToList.html", description: _("Command-separated list of email address that should be in the Reply-To header for this project.")) {
  f.textbox(name: "project_replyto", value: configured ? instance.replyTo : "\$DEFAULT_REPLYTO", checkUrl: "'${rootURL}/publisher/ExtendedEmailPublisher/recipientListRecipientsCheck?value='+encodeURIComponent(this.value)") 
}
f.entry(title: _("Content Type"), help: "/plugin/email-ext/help/projectConfig/contentType.html") {
  select(name: "project_content_type", class: "setting-input") {
    f.option(selected: 'default'==instance?.contentType, value: "default", _("Default Content Type")) 
    f.option(selected: 'text/plain'==instance?.contentType, value: "text/plain", _("projectContentType.plainText")) 
    f.option(selected: 'text/html'==instance?.contentType, value: "text/html", _("projectContentType.html")) 
  }
}
f.entry(title: _("Default Subject"), help: "/plugin/email-ext/help/projectConfig/defaultSubject.html") {
  f.textbox(name: "project_default_subject", value: configured ? instance.defaultSubject : "\$DEFAULT_SUBJECT") 
}
f.entry(title: _("Default Content"), help: "/plugin/email-ext/help/projectConfig/defaultBody.html") {
  f.textarea(name: "project_default_content", value: configured ? instance.defaultContent : "\$DEFAULT_CONTENT") 
}
f.entry(title: _("Attachments"), help: "/plugin/email-ext/help/projectConfig/attachments.html", description: _("description", "http://ant.apache.org/manual/Types/fileset.html")) {
  f.textbox(name: "project_attachments", value: configured ? instance.attachmentsPattern : "") 
}
f.entry(title: _("Attach Build Log"), help: "/plugin/email-ext/help/projectConfig/attachBuildLog.html") {
  select(name:"project_attach_buildlog") {
    f.option(value: 0, selected: instance != null ? !instance.attachBuildLog : true, _("Do Not Attach Build Log"))
    f.option(value: 1, selected: instance != null ? instance.attachBuildLog && !instance.compressBuildLog : false, _("Attach Build Log"))
    f.option(value: 2, selected: instance != null ? instance.attachBuildLog && instance.compressBuildLog : false, _("Compress and Attach Build Log"))
  }      
}

f.entry(title: _("Content Token Reference"), field: "tokens")

if(descriptor.isMatrixProject(my)) {
  f.entry(field: "matrixTriggerMode", title: _("Trigger for matrix projects")) {
    f.enum { 
      raw(my.description)
    }
  }
}

f.advanced() {
  f.entry(title: _("Pre-send Script"), help: "/plugin/email-ext/help/projectConfig/presendScript.html") {
    f.textarea(id: "project_presend_script", name: "project_presend_script", value: configured ? instance.presendScript : "\$DEFAULT_PRESEND_SCRIPT", class: "setting-input") 
  }

  f.entry(title: _("Save Generated E-mail to Workspace"), help: "/plugin/email-ext/help/projectConfig/saveOutput.html") {
    f.checkbox(name: "project_save_output", checked: instance?.saveOutput)
  }
  
  f.entry(title: _("Triggers"), help: "/plugin/email-ext/help/projectConfig/triggers.html") {
    f.hetero_list(name: "project_triggers", hasHeader: true, descriptors: triggers, items: instance?.configuredTriggers, addCaption:_("Add Trigger"), deleteCaption: _("Remove Trigger"))
  }
}


