
def name = 'chapter-salesforce'

organizationFolder(name) {
    displayName(name)
    multibranchPipelineJob('sfdx-create-scratch-org') {
        displayName('SFDX Create Scratch Orgs')
        branchSources {
            git {
                id('123')
                remote('git@github.com:sumup/sfdc.git')
                credentialsId('github')
                includes('quality')
            }
        }
    }
}