package net.thumbtack.thumbnote.thumbnoteroot.elastic.repository;

import net.thumbtack.thumbnote.thumbnoteroot.elastic.model.ElasticNote;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface NoteElasticRepository extends ElasticsearchRepository<ElasticNote, Integer> {
    ElasticNote save(ElasticNote note);
}
