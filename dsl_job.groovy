
def name = 'chapter-salesforce'

organizationFolder(name) {
    displayName(name)
    multibranchPipelineJob('SFDX-Create Scrach Org') {

    }
}