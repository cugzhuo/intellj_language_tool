package com.github.cugzhuo.intelljlanguagetool.services

import com.github.cugzhuo.intelljlanguagetool.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
