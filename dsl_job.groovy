import utils.*

def name = 'chapter-salesforce'

listView(name) {
    jobs {
        name(name)
        regex(name+'.+')
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

new GithubSingleBranchPipeline()
    .viewPrefix(name)
    .repository('sfdc')
    .jenkinsfileLocation('Jenkinsfile.scratch-org')
    .branch('refs/heads/quality')
    .build(this)
