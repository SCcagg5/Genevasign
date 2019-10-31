from Model.basic import check, auth
from Object.hortis import sign


def getauth(cn, nextc):
    err = check.contain(cn.pr, ["pass"])
    if not err[0]:
        return cn.toret.add_error(err[1], err[2])
    cn.pr = err[1]
    err = auth.gettoken(cn.pr["pass"])
    return cn.call_next(nextc, err)

def myauth(cn, nextc):
    err = check.contain(cn.pr, ["token"])
    if not err[0]:
        return cn.toret.add_error(err[1], err[2])
    cn.pr = err[1]
    err = auth.verify(cn.pr["token"])
    return cn.call_next(nextc, err)

def signdoc(cn, nextc):
    err = check.contain(cn.pr, ["id", "firstname", "lastname", "digest", "name"])
    if not err[0]:
        return cn.toret.add_error(err[1], err[2])
    cn.pr = err[1]

    use = sign(cn.pr["id"], cn.pr["firstname"], cn.pr["lastname"])
    err = use.push(cn.pr["name"], cn.pr["digest"])
    return cn.call_next(nextc, err)
