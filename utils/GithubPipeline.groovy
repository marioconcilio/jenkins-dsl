package utils

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.jobs.MultibranchWorkflowJob

// Created to make it easy to creade new jobs without duplicating the code

@Builder(builderStrategy = SimpleStrategy, prefix = '')
class GithubPipeline {
    String viewPrefix
    String repository
    String jobSuffix = ""
    String includes = "*"
    String jenkinsfileLocation = "Jenkinsfile"
    Boolean discoverBranches = true
    Boolean discoverTags = true
    Boolean specifyMasterRefSpec = false

    MultibranchWorkflowJob build(DslFactory dslFactory) {
        def cleanedRepoString = this.repository.toLowerCase().replaceAll(/_/, '-');

        def jobId = "${this.viewPrefix.toLowerCase()}-${cleanedRepoString}"
        if (this.jobSuffix) jobId += "-${jobSuffix}"

        dslFactory.multibranchPipelineJob(jobId) {
            branchSources {
                github {
                    id(jobId)
                    includes(this.includes)
                    repoOwner('sumup')
                    repository(this.repository)
                    scanCredentialsId('github-appscisumup-dev-token')
                    checkoutCredentialsId('github-appscisumup-dev-token')
                }
            }
            factory {
                workflowBranchProjectFactory {
                    scriptPath(this.jenkinsfileLocation)
                }
            }
            configure {
                def traits = it / sources / data / 'jenkins.branch.BranchSource' / source / traits
                if (this.discoverBranches) {
                    traits << 'org.jenkinsci.plugins.github_branch_source.BranchDiscoveryTrait' {
                        strategyId(3) // detect all branches -refer the plugin source code for various options
                    }
                }
                if (this.discoverTags) {
                    traits << 'org.jenkinsci.plugins.github_branch_source.TagDiscoveryTrait' { }
                }
                if (this.specifyMasterRefSpec) {
                    traits << 'jenkins.plugins.git.traits.RefSpecsSCMSourceTrait' {
                        templates {
                            'jenkins.plugins.git.traits.RefSpecsSCMSourceTrait_-RefSpecTemplate' {
                                value('+refs/heads/master:refs/remotes/origin/master')
                            }
                        }
                    }
                }
            }
            orphanedItemStrategy {
                discardOldItems {
                    daysToKeep(20)
                    numToKeep(25)
                }
            }
        }
    }
}
