import utils.*

def chapterName = 'chapter-salesforce'
  
listView(chapterName) {
    jobs {
      	regex("${chapterName}.+")
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
    .viewPrefix(chapterName)
    .repository('sfdc')
    .jenkinsfileLocation('Jenkinsfile.scratch-org')
    .branch('refs/heads/quality')
    .build(this)

new GithubPipeline()
    .viewPrefix('chapter')
    .repository('salesforce')
    .includes('master quality dev')
    .jenkinsfileLocation('Jenkinsfile')
    .build(this)