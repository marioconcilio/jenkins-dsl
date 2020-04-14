
def name = 'chapter-salesforce'

organizationFolder(name) {
    displayName(name)
    multibranchPipelineJob('sfdx-create-scratch-org') {
        displayName('SFDX Create Scratch Orgs')
        branchSources {
            git {
                remote('git@github.com:sumup/sfdc.git')
                credentialsId('github')
                includes('quality')
            }
        }
    }
}