package utils

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.jobs.WorkflowJob

// for the non-multibranch pipelines

@Builder(builderStrategy = SimpleStrategy, prefix = '')
class GithubSingleBranchPipeline {
    String viewPrefix
    String repository
    String jenkinsfileLocation = "Jenkinsfile"
    String branch = "refs/heads/master"
    String cronTrigger = ""
    Closure urlTrigger = null
    String authenticationToken = ""

    WorkflowJob build(DslFactory dslFactory) {
        def cleanedRepoString = this.repository.toLowerCase().replaceAll(/_/, '-')
        def ownerAndProject = "sumup/" + this.repository

        def jobId = "${this.viewPrefix.toLowerCase()}-${cleanedRepoString}"

        dslFactory.pipelineJob(jobId) {
            definition {
                cpsScm {
                    scm {
                        git {
                            remote {
                                github(ownerAndProject, "ssh")
                                credentials('appscisumup')
                                branch(this.branch)
                            }
                        }
                    }
                    scriptPath(this.jenkinsfileLocation)
                }
            }
            if (this.cronTrigger) {
                triggers {
                    cron(this.cronTrigger)
                }
            }
            if (this.urlTrigger != null) {
                triggers {
                    urlTrigger(this.urlTrigger)
                }
            }
            if (this.authenticationToken) {
                authenticationToken(this.authenticationToken)
            }
        }
    }
}
